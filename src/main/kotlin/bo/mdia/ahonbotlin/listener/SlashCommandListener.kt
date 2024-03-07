package bo.mdia.ahonbotlin.listener

import bo.mdia.ahonbotlin.command.SlashCommand
import discord4j.core.GatewayDiscordClient
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class SlashCommandListener(
  @Autowired private val commands: List<SlashCommand>,
  @Autowired private val gatewayDiscordClient: GatewayDiscordClient,
) : CommandListener {
  init {
    gatewayDiscordClient.on(ChatInputInteractionEvent::class.java) { e -> handle(e) }.subscribe()
  }

  override fun handle(event: ChatInputInteractionEvent): Mono<Void> =
    Flux.fromIterable(commands)
      .filter { it.name == event.commandName }
      .next()
      .flatMap { it.handle(event) }
}
