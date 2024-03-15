package bo.mdia.ahonbotlin.listener

import bo.mdia.ahonbotlin.command.SlashCommand
import discord4j.core.GatewayDiscordClient
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import io.klogging.NoCoLogging
import kotlinx.coroutines.reactor.mono
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.scheduler.Schedulers

@Component
class SlashCommandListener(
  @Autowired private val commands: List<SlashCommand>,
  @Autowired private val discordGateway: GatewayDiscordClient,
) : CommandListener, NoCoLogging {
  init {
    discordGateway
      .on(ChatInputInteractionEvent::class.java) { mono { handle(it) } }
      .publishOn(Schedulers.boundedElastic())
      .doOnError { logger.error { "Error handling slash command: $it" } }
      .subscribe()
  }

  override suspend fun handle(event: ChatInputInteractionEvent) =
    commands.first { it.name == event.commandName }.handle(event)
}
