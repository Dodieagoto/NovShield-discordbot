package dodia.novshield.discordbot.tickets.panels

import dodia.novshield.discordbot.tickets.Panel
import dodia.novshield.discordbot.tickets.ModalField

import net.dv8tion.jda.api.components.textinput.TextInputStyle
import dev.minn.jda.ktx.interactions.components.Separator
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.components.container.Container
import net.dv8tion.jda.api.components.textinput.TextInput
import net.dv8tion.jda.api.modals.Modal
import net.dv8tion.jda.api.components.label.Label
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.components.textdisplay.TextDisplay



object TerritoryReg : Panel(
    logChannel = "1507447988514062397",
    panelHEX = 0xFF0000,
    supportRole = "1506319765730234379",
    ticketCategory = "1506320009293467728",
    channelPrefix = "территория"
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
        val pool3 = TextInput.create(field3.id, field3.style)
            .setPlaceholder(field3.placeholder)
            .setRequired(field3.required)

            .build()

        val modal = Modal.create("Territory_panel", "🗺️ Регистрация территории")
            .addComponents(
                Label.of(field1.label, pool1),
                Label.of(field2.label, pool2),
                Label.of(field3.label, pool3),

            )

            .build()

        event.replyModal(modal).queue()
    }

        override fun buildTicketMessage(event: ModalInteractionEvent): Container {
            val field1 = event.getValue("Territory_field_1")
                ?.asString ?: "Нет данных"

            val field2 = event.getValue("Territory_field_2")
                ?.asString ?: "Нет данных"

            val field3 = event.getValue("Territory_field_3")
                ?.asString ?: "Нет данных"

            return Container.of(
                TextDisplay.of("🗺️ Регистрация территории"),
                Separator(),
                TextDisplay.of("**Координаты территории:** $field1"),
                TextDisplay.of("**Границы территории:** $field2"),
                TextDisplay.of("**Владельцы территории:** $field3")
            )
        }

    val field1 = ModalField(
        label = "Укажите координаты территории",
        id = "Territory_field_1",
        placeholder = "Т.е примерный центр территории",
        required = true,
        style = TextInputStyle.SHORT
    )

    val field2 = ModalField(
        label = "Укажите границы вашей территории",
        id = "Territory_field_2",
        placeholder = "Например 300 на 300 блоков или желаемые чанки",
        required = true,
        style = TextInputStyle.SHORT
    )
    val field3 = ModalField(
        label = "Напишите никнеймы владельца(ов) территории",
        id = "Territory_field_3",
        placeholder = "Важно, чтобы никнеймы были написаны в точности, как в игре",
        required = true,
        style = TextInputStyle.PARAGRAPH
    )

}