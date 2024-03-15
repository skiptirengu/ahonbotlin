package bo.mdia.ahonbotlin.command

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import discord4j.discordjson.json.ImmutableApplicationCommandRequest

interface SlashCommand {
  val name: String

  suspend fun handle(event: ChatInputInteractionEvent)

  fun definition(): ImmutableApplicationCommandRequest
}
