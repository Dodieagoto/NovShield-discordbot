package dodia.novshield.discordbot.tickets.panels

import dodia.novshield.discordbot.tickets.Panel
import dodia.novshield.discordbot.tickets.ModalField
import net.dv8tion.jda.api.components.actionrow.ActionRow

import net.dv8tion.jda.api.components.textinput.TextInputStyle
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.components.container.Container
import net.dv8tion.jda.api.components.textinput.TextInput
import net.dv8tion.jda.api.modals.Modal
import net.dv8tion.jda.api.components.label.Label
import net.dv8tion.jda.api.components.separator.Separator
import net.dv8tion.jda.api.components.textdisplay.TextDisplay
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder
import java.time.format.DateTimeFormatter

import dodia.novshield.discordbot.tickets.database.Ticket

object Patent : Panel(
    logChannel = "1507447988514062397",
    panelHEX = 0xFF0000,
    supportRole = "1506319765730234379",
    ticketCategory = "1508061390089228378",
    channelPrefix = "патент"
) {
    override fun sendTicketLog(event: ButtonInteractionEvent, dbTicket: Ticket) {
        val jda = event.jda
        val logTextChannel = jda.getTextChannelById(this.logChannel) ?: return

        val field1Val = dbTicket.fields.find { it.fieldLabel == "Patent_field_1" }?.fieldValue ?: "Нет данных"
        val field2Val = dbTicket.fields.find { it.fieldLabel == "Patent_field_2" }?.fieldValue ?: "Нет данных"
        val field3Val = dbTicket.fields.find { it.fieldLabel == "Patent_field_3" }?.fieldValue ?: "Нет данных"

        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
        val closedTime = dbTicket.closedAt?.format(formatter) ?: "Неизвестно"

        val logContainer = Container.of(
            TextDisplay.of("📁 **Архив тикета №${dbTicket.id} [Заявка на патент]**"),
            Separator.createDivider(Separator.Spacing.LARGE),

            TextDisplay.of("**Информация:**\n• Заявитель (Автор): <@${dbTicket.authorId}>\n• Закрыл: ${event.user.asMention}\n• Время закрытия: $closedTime"),
            Separator.createDivider(Separator.Spacing.LARGE),

            TextDisplay.of("**На что патент?:**\n$field1Val"),
            Separator.createDivider(Separator.Spacing.LARGE),
            TextDisplay.of("**Доказательства:**\n$field2Val"),
            Separator.createDivider(Separator.Spacing.LARGE),
            TextDisplay.of("**Обоснование:**\n$field3Val")
        ).withAccentColor(this.panelHEX)

        val logMessage = MessageCreateBuilder()
            .useComponentsV2()
            .setComponents(logContainer)
            .build()

        logTextChannel.sendMessage(logMessage).queue()
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

        val modal = Modal.create("Patent_panel", "📜 Подача патента")
            .addComponents(
                Label.of(field1.label, pool1),
                Label.of(field2.label, pool2),
                Label.of(field3.label, pool3),
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

        val container = Container.of(
            TextDisplay.of("📜 Подача патента"),
            Separator.createDivider(Separator.Spacing.LARGE),
            TextDisplay.of("**Причина подачи патента:**\n$field1"),
            Separator.createDivider(Separator.Spacing.LARGE),
            TextDisplay.of("**Краткое описание дела:**\n$field2"),
            Separator.createDivider(Separator.Spacing.LARGE),
            TextDisplay.of("**Требования:**\n$field3"),

            )

        val btnContainer = Container.of(
            ActionRow.of(
                closeButton
            )
        )

        return listOf(container, btnContainer)

    }

    val field1 = ModalField(
        label = "На что патент?",
        id = "Patent_field_1",
        placeholder = "Например: постройка, песня, мап-арт и т.п",
        required = true,
        style = TextInputStyle.SHORT
    )

    val field2 = ModalField(
        label = "Доказательства",
        id = "Patent_field_2",
        placeholder = "Например, если это ваш скин, то скиньте файл и скриншот формы (ссылкой)",
        required = true,
        style = TextInputStyle.SHORT
    )
    val field3 = ModalField(
        label = "Дайте чёткое обоснование",
        id = "Patent_field_3",
        placeholder = "Поясните, почему вам должны оформить патент",
        required = true,
        style = TextInputStyle.PARAGRAPH
    )

}