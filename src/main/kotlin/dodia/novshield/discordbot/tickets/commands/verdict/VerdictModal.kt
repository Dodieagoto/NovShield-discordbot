package dodia.novshield.discordbot.tickets.commands.verdict

import dodia.novshield.discordbot.tickets.panels.ModerComplaint.field1
import net.dv8tion.jda.api.components.container.Container
import java.awt.Color
import net.dv8tion.jda.api.components.textdisplay.TextDisplay
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder
import net.dv8tion.jda.api.components.label.Label
import net.dv8tion.jda.api.components.selections.EntitySelectMenu
import net.dv8tion.jda.api.components.selections.StringSelectMenu
import net.dv8tion.jda.api.components.textinput.TextInput
import net.dv8tion.jda.api.components.textinput.TextInputStyle
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.modals.Modal
import net.dv8tion.jda.api.interactions.commands.CommandInteraction

object VerdictModal {

    private val ALLOWED_ROLE = setOf(
        "1506338573631356958",
        "1506319039012540487",
    )

    fun showModal(event: CommandInteraction){

        val member = event.member ?: return

        val hasRole = member.roles.any { it.id in ALLOWED_ROLE }

        if (!hasRole){
            val container = Container.of(
                TextDisplay.of("❌ У тебя нет прав на использование этой команды!")
            ).withAccentColor(Color(0xFF535B).rgb)

            val message = MessageCreateBuilder()
                .useComponentsV2()
                .setComponents(container)
                .build()

            event.reply(message)
                .setEphemeral(true)
                .queue()
            return
        }

        val verdictInput = TextInput.create("ticket-verdict", TextInputStyle.PARAGRAPH)
            .setPlaceholder("Введите текст вердикта:")
            .setRequired(true)

            .build()

        val accusations = TextInput.create("ticket-accusations", TextInputStyle.PARAGRAPH)
            .setPlaceholder("Введите краткое описание дела:")
            .setRequired(true)

            .build()

        val stringSelect = StringSelectMenu.create("verdict-select")
            .addOption(
                "Виновен",
                "SelectMenu_verdict_vinoven",
                "Да, ответчик виновен",
                Emoji.fromUnicode("\uD83D\uDD17")
            )
            .addOption(
                "Не виновен",
                "SelectMenu_verdict_nevinoven",
                "Нет, ответчик не виновен",
                Emoji.fromUnicode("⛓\uFE0F\u200D\uD83D\uDCA5")
            )

            .build()

        val plaintiffSide = EntitySelectMenu.create("verdict-plaint", EntitySelectMenu.SelectTarget.USER)
            .setMaxValues(10)
            .build()

        val defendantSide = EntitySelectMenu.create("verdict-defend", EntitySelectMenu.SelectTarget.USER)
            .setMaxValues(10)
            .build()

        val modal = Modal.create("verdict-modal","Панель вынесения вердикта")
            .addComponents(

                Label.of(
                    "Вердикт",
                    verdictInput
                ),
                Label.of(
                    "Суть дела",
                    accusations
                ),
                Label.of(
                    "Виновен ли ответчик?",
                    stringSelect
                ),

                Label.of(
                    "Сторона истца",
                    plaintiffSide
                ),

                Label.of(
                    "Сторона ответчика",
                    defendantSide
                ),

            )

            .build()

        event.replyModal(modal).queue()
    }

}