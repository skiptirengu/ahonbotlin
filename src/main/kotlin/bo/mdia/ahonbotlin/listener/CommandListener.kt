package bo.mdia.ahonbotlin.listener

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import reactor.core.publisher.Mono

interface CommandListener {
  fun handle(event: ChatInputInteractionEvent): Mono<Void>
}
