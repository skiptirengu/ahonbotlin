package bo.mdia.ahonbotlin.command

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import discord4j.core.`object`.command.ApplicationCommandOption
import discord4j.discordjson.json.ApplicationCommandOptionData
import discord4j.discordjson.json.ApplicationCommandRequest
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Component

@Component
class AvatarCommand : SlashCommand {
  override val name: String = "avatar"

  override fun definition() =
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

  override suspend fun handle(event: ChatInputInteractionEvent) {
    event
      .reply()
      .withContent(
        "${event.getOption("user").get().value.get().asUser().awaitSingle().avatarUrl}?size=256"
      )
      .withEphemeral(false)
      .doOnError { error("Error sending avatar: $it") }
      .awaitSingleOrNull()
  }
}
