package dodia.novshield.discordbot.tickets.commands.verdict

import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.components.container.Container
import net.dv8tion.jda.api.components.textdisplay.TextDisplay
import net.dv8tion.jda.api.components.separator.Separator
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder

object VerdictModalHandler {

    fun onModalHandler(event: ModalInteractionEvent, guild: Guild) {

        val modalVerdict = event.getValue("ticket-verdict")?.asString
        val modalAccusations = event.getValue("ticket-accusations")?.asString
        val modalIsGuilty = event.getValue("verdict-select")?.getAsStringList()
        val plaintiffSide = event.getValue("verdict-plaint")?.getAsStringList()
        val defendantSide = event.getValue("verdict-defend")?.getAsStringList()

        fun mentionUsers(ids: List<String>?): String {
            return ids
                ?.joinToString(" ") { "<@$it>" }
                ?: "Никто"
        }

        val plaintiffsMentions = mentionUsers(plaintiffSide)
        val defendantsMentions = mentionUsers(defendantSide)

        lateinit var isGuilty: String

        when (modalIsGuilty?.first()){
            "SelectMenu_verdict_vinoven" -> {
                isGuilty = ":link: виновен"
            }

            "SelectMenu_verdict_nevinoven" -> {
                isGuilty = ":broken_chain: не виновен"
            }
        }



        val forum = guild.getForumChannelById("1513573523493228735")

        val container = Container.of(
            TextDisplay.of("> ## \uD83E\uDEAA Вердикт дела ${plaintiffSide?.first()}"),
            TextDisplay.of("Судья: ${event.user.asMention}"),
            Separator.createDivider(Separator.Spacing.LARGE),
            TextDisplay.of("- Сторона истца: $plaintiffsMentions"),
            TextDisplay.of("- Сторона ответчика: $defendantsMentions"),
            Separator.createDivider(Separator.Spacing.SMALL),
            TextDisplay.of("Суть дела: \n $modalAccusations"),
            TextDisplay.of("Виновен ли ответчик: $isGuilty"),
            TextDisplay.of("- Вердикт: \n```$modalVerdict```")
        )

        val message = MessageCreateBuilder()
            .useComponentsV2()
            .setComponents(container)
            .build()

        forum?.createForumPost("Вердикт дела ${plaintiffSide?.first()}", message)?.queue()

    }

}