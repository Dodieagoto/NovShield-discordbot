package dodia.novshield.discordbot.tickets.panels

import dodia.novshield.discordbot.tickets.ModalField
import dodia.novshield.discordbot.tickets.Panel
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

object ParliamentQuestion : Panel(
    logChannel = "1507448073155121242",
    panelHEX = 0xFF0000,
    supportRole = "1506335312295497758",
    ticketCategory = "1508061015776952430",
    channelPrefix = "парламент"
){
    override fun sendTicketLog(event: ButtonInteractionEvent, dbTicket: Ticket) {
        val jda = event.jda
        val logTextChannel = jda.getTextChannelById(this.logChannel) ?: return

        val field1Val = dbTicket.fields.find { it.fieldLabel == "Parliament_field_1" }?.fieldValue ?: "Нет данных"
        val field2Val = dbTicket.fields.find { it.fieldLabel == "Parliament_field_2" }?.fieldValue ?: "Нет данных"

        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
        val closedTime = dbTicket.closedAt?.format(formatter) ?: "Неизвестно"

        val logContainer = Container.of(
            TextDisplay.of("📁 **Архив тикета №${dbTicket.id} [Обращение в Парламент]**"),
            Separator.createDivider(Separator.Spacing.LARGE),

            TextDisplay.of("**Информация:**\n• Автор: <@${dbTicket.authorId}>\n• Закрыл: ${event.user.asMention}\n• Время закрытия: $closedTime"),
            Separator.createDivider(Separator.Spacing.LARGE),

            TextDisplay.of("**Никнейм:**\n$field1Val"),
            Separator.createDivider(Separator.Spacing.LARGE),
            TextDisplay.of("**Вопрос:**\n$field2Val")
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

            .build()

        val modal = Modal.create("Parliament_panel", "🏛️ Подача вопроса в парламент")
            .addComponents(
                Label.of(field1.label, pool1),
                Label.of(field2.label, pool2)
            )

            .build()

        event.replyModal(modal).queue()
    }

    override fun buildTicketMessage(event: ModalInteractionEvent): List<Container> {

        val field1 = event.getValue("Parliament_field_1")
            ?.asString ?: "Нет данных"

        val field2 = event.getValue("Parliament_field_2")
            ?.asString ?: "Нет данных"


        val container = Container.of(
            TextDisplay.of("Вопрос парламент"),
            Separator.createDivider(Separator.Spacing.LARGE),
            TextDisplay.of("**Ваш никнейм:**\n$field1"),
            Separator.createDivider(Separator.Spacing.LARGE),
            TextDisplay.of("**Ваш вопрос:**\n$field2"),

            )

        val btnContainer = Container.of(
            ActionRow.of(
                closeButton
            )
        )

        return listOf(container, btnContainer)

    }

    val field1 = ModalField(
        label = "Ваш никнейм",
        id = "Parliament_field_1",
        placeholder = "Никнейм",
        required = true,
        style = TextInputStyle.SHORT
    )

    val field2 = ModalField(
        label = "Напишите свой вопрос",
        id = "Parliament_field_2",
        placeholder = "Рекомендуется сформулировать вопрос корректно во избежание недопониманий",
        required = true,
        style = TextInputStyle.PARAGRAPH
    )
}