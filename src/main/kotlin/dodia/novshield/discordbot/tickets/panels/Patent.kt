package dodia.novshield.discordbot.tickets.panels

import dodia.novshield.discordbot.tickets.Panel
import dodia.novshield.discordbot.tickets.ModalField

import net.dv8tion.jda.api.components.textinput.TextInputStyle
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.components.container.Container
import net.dv8tion.jda.api.components.textinput.TextInput
import net.dv8tion.jda.api.modals.Modal
import net.dv8tion.jda.api.components.label.Label
import net.dv8tion.jda.api.components.separator.Separator
import net.dv8tion.jda.api.components.textdisplay.TextDisplay
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent

object Patent : Panel(
    logChannel = "1507447988514062397",
    panelHEX = 0xFF0000,
    supportRole = "1506319765730234379",
    ticketCategory = "1506320009293467728",
    channelPrefix = "патент"
) {
    override fun sendTicketLog(){

    }

    override fun showModal(event: StringSelectInteractionEvent) {

        val pool1 = TextInput.create(field1.id, field1.style)
            .setPlaceholder(field1.placeholder)
            .setRequired(field1.required)

            .build()

        val pool2 = TextInput.create(field2.id, field2.style)
            .setPlaceholder(field2.placeholder)
            .setRequired(field2.required)
            .setMinLength(15)

            .build()

        val pool3 = TextInput.create(field3.id, field3.style)
            .setPlaceholder(field3.placeholder)
            .setRequired(field3.required)

            .build()

        val pool4 = TextInput.create(field4.id, field4.style)
            .setPlaceholder(field4.placeholder)
            .setRequired(field4.required)

            .build()

        val modal = Modal.create("Patent_panel", "📜 Подача патента")
            .addComponents(
                Label.of(field1.label, pool1),
                Label.of(field2.label, pool2),
                Label.of(field3.label, pool3),
                Label.of(field4.label, pool4)
            )

            .build()

        event.replyModal(modal).queue()


    }


    override fun buildTicketMessage(event: ModalInteractionEvent): List<Container> {

        val field1 = event.getValue("Patent_field_1")
            ?.asString ?: "Нет данных"

        val field2 = event.getValue("Patent_field_2")
            ?.asString ?: "Нет данных"

        val field3 = event.getValue("Patent_field_3")
            ?.asString ?: "Нет данных"

        val field4 = event.getValue("Patent_field_4")
            ?.asString ?: "Нет данных"

        val container = Container.of(
            TextDisplay.of("📜 Подача патента"),
            Separator.createDivider(Separator.Spacing.LARGE),
            TextDisplay.of("**Причина подачи патента:**\n$field1"),
            Separator.createDivider(Separator.Spacing.LARGE),
            TextDisplay.of("**Краткое описание дела:**\n$field2"),
            Separator.createDivider(Separator.Spacing.LARGE),
            TextDisplay.of("**Требования:**\n$field3"),
            Separator.createDivider(Separator.Spacing.LARGE),
            TextDisplay.of("**Доказательства:**\n$field4"),

            )

        return listOf(container)

    }

    val field1 = ModalField(
        label = "Укажите причину подачи",
        id = "Patent_field_1",
        placeholder = "Например: убийство, кража, гриф и т.п",
        required = true,
        style = TextInputStyle.SHORT
    )

    val field2 = ModalField(
        label = "Кратко опишите ваше дело",
        id = "Patent_field_2",
        placeholder = "Не забудьте указать на кого вы подаёте дело",
        required = true,
        style = TextInputStyle.SHORT
    )
    val field3 = ModalField(
        label = "Укажите требования",
        id = "Patent_field_3",
        placeholder = "Важно, чтобы требования были в рамках разумного",
        required = true,
        style = TextInputStyle.PARAGRAPH
    )

    val field4 = ModalField(
        label = "Предоставьте доказательства",
        id = "Patent_field_4",
        placeholder = "Запись экрана, скриншоты и т.п (ссылкой)",
        required = true,
        style = TextInputStyle.PARAGRAPH
    )
}