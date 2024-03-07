package bo.mdia.ahonbotlin.command

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import discord4j.discordjson.json.ImmutableApplicationCommandRequest
import reactor.core.publisher.Mono

interface SlashCommand {
  val name: String

  fun handle(event: ChatInputInteractionEvent): Mono<Void>

  fun request(): ImmutableApplicationCommandRequest
}
