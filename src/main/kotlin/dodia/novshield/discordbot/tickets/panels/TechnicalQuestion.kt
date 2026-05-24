package dodia.novshield.discordbot.tickets.panels


import dodia.novshield.discordbot.tickets.Panel
import dodia.novshield.discordbot.tickets.ModalField

import dev.minn.jda.ktx.interactions.components.Separator
import net.dv8tion.jda.api.components.textinput.TextInputStyle
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.components.container.Container
import net.dv8tion.jda.api.components.textinput.TextInput
import net.dv8tion.jda.api.modals.Modal
import net.dv8tion.jda.api.components.label.Label
import net.dv8tion.jda.api.components.textdisplay.TextDisplay
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent

object TechnicalQuestion : Panel(
    logChannel = "1507448073155121242",
    panelHEX = 0xFF0000,
    supportRole = "1506319265400225803",
    ticketCategory = "1506320009293467728",
    channelPrefix = "тех-вопрос"
){
    override fun sendTicketLog() {
        //todo() сделать логирование
    }

    override fun showModal(event: StringSelectInteractionEvent) {

        val pool1 = TextInput.create(field1.id, field1.style)
            .setPlaceholder(field1.placeholder)
            .setRequired(field1.required)

            .build()

        val pool2 = TextInput.create(field2.id, field2.style)
            .setPlaceholder(field2.placeholder)
            .setRequired(field2.required)

            .build()

        val modal = Modal.create("TechnicalQuestion_panel", "🛠️ Подача технического вопроса")
            .addComponents(
                Label.of(field1.label, pool1),
                Label.of(field2.label, pool2)
            )

            .build()
        event.replyModal(modal).queue()
    }

    override fun buildTicketMessage(event: ModalInteractionEvent): List<Container> {

        val field1 = event.getValue("Tech_field_1")
            ?.asString ?: "Нет данных"

        val field2 = event.getValue("Tech_field_2")
            ?.asString ?: "Нет данных"

        val container = Container.of(
            TextDisplay.of("🛠️ Технический вопрос"),
            Separator(),
            TextDisplay.of("**Никнейм:** $field1"),
            TextDisplay.of("**Вопрос:** $field2")
        )

        return listOf(container)

    }

    val field1 = ModalField(
        label = "Ваш никнейм",
        id = "Tech_field_1",
        placeholder = "Никнейм",
        required = true,
        style = TextInputStyle.SHORT
    )

    val field2 = ModalField(
        label = "Напишите свой вопрос",
        id = "Tech_field_2",
        placeholder = "Рекомендуется сформулировать вопрос корректно во избежание недопониманий",
        required = true,
        style = TextInputStyle.PARAGRAPH
    )

}
