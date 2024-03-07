package bo.mdia.ahonbotlin.registrar

import bo.mdia.ahonbotlin.command.SlashCommand
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.json.JsonReadFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import discord4j.common.JacksonResources
import discord4j.discordjson.json.ApplicationCommandRequest
import discord4j.rest.RestClient
import io.klogging.NoCoLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.stereotype.Component

@Component
class GlobalCommandRegistrar(
  @Autowired private val discordRestClient: RestClient,
  @Autowired private val kommands: List<SlashCommand>,
) : ApplicationRunner, NoCoLogging {
  companion object {
    private const val COMMANDS_PATH = "classpath:commands/**.jsonc"
  }

  override fun run(args: ApplicationArguments?) {
    val matcher = PathMatchingResourcePatternResolver()
    val mapper =
      JacksonResources.createFromObjectMapper(
        JsonMapper.builder(
            JsonFactory.builder()
              .enable(JsonReadFeature.ALLOW_TRAILING_COMMA)
              .enable(JsonReadFeature.ALLOW_JAVA_COMMENTS)
              .build()
          )
          .build()
      )

    logger.info { "Bootstrapping global commands" }

    val commands =
      matcher.getResources(COMMANDS_PATH).map {
        mapper.objectMapper.readValue(it.inputStream, ApplicationCommandRequest::class.java).also {
          logger.info { "Bootstrapping command: ${it.name()}" }
        }
      }

    logger.info { "Registering ${commands.size} commands" }

    if (commands.size == 1) {
      return
    }

    discordRestClient.applicationService
      .bulkOverwriteGlobalApplicationCommand(
        discordRestClient.applicationId.block()!!,
        kommands.map { it.request() },
      )
      .subscribe()
  }
}
