package bo.mdia.ahonbotlin.listener

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent

interface CommandListener {
  suspend fun handle(event: ChatInputInteractionEvent)
}
