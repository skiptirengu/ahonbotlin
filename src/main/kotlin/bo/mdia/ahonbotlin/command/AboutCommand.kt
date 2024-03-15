package bo.mdia.ahonbotlin.command

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import discord4j.core.spec.EmbedCreateSpec
import discord4j.discordjson.json.ApplicationCommandRequest
import discord4j.discordjson.json.ImmutableApplicationCommandRequest
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Component

@Component
class AboutCommand : SlashCommand {
  companion object {
    private const val GITHUB_URL = "https://github.com/skiptirengu/ahonbotlin"
  }

  override val name: String = "about"

  override suspend fun handle(event: ChatInputInteractionEvent) {
    val embed =
      EmbedCreateSpec.builder()
        .title("Your average discord bot")
        .url("https://github.com/skiptirengu/ahonbotlin")
        .addField("License", "WTFPL", false)

    event.client.self.awaitSingleOrNull()?.avatarUrl?.let {
      embed.thumbnail(it)
      embed.author("Me", GITHUB_URL, it)
    }

    event.reply().withEmbeds(embed.build()).awaitSingleOrNull()
  }

  override fun definition(): ImmutableApplicationCommandRequest =
    ApplicationCommandRequest.builder().name(name).description("About the bot").build()
}
