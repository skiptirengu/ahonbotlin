package bo.mdia.ahonbotlin.command

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import discord4j.core.`object`.command.ApplicationCommandOption
import discord4j.discordjson.json.ApplicationCommandOptionData
import discord4j.discordjson.json.ApplicationCommandRequest
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AvatarCommand(override val name: String = "avatar") : SlashCommand {

  override fun request() =
    ApplicationCommandRequest.builder()
      .name(name)
      .description("Get a user's avatar")
      .addOption(
        ApplicationCommandOptionData.builder()
          .name("user")
          .description("The user to get the avatar of")
          .type(ApplicationCommandOption.Type.USER.value)
          .required(true)
          .build()
      )
      .build()

  override fun handle(event: ChatInputInteractionEvent): Mono<Void> {
    return event
      .reply()
      .withContent(
        event
          .getOption("user")
          .flatMap { it.value }
          .map { "${it.asUser().block()!!.avatarUrl}?size=256" }
          .get()
      )
      .withEphemeral(false)
  }
}
