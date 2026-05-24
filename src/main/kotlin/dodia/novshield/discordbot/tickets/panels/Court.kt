package dodia.novshield.discordbot.tickets.panels

import dodia.novshield.discordbot.tickets.ModalField
import dodia.novshield.discordbot.tickets.Panel

import net.dv8tion.jda.api.components.textinput.TextInput
import net.dv8tion.jda.api.modals.Modal
import net.dv8tion.jda.api.components.label.Label
import net.dv8tion.jda.api.components.textinput.TextInputStyle
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.components.textdisplay.TextDisplay
import net.dv8tion.jda.api.components.separator.Separator
import net.dv8tion.jda.api.components.container.Container
import net.dv8tion.jda.api.components.actionrow.ActionRow

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder
import java.time.format.DateTimeFormatter

import dodia.novshield.discordbot.tickets.database.Ticket


object Court : Panel(
    logChannel = "1507440553673883738",
    panelHEX = 0xFF0000,
    supportRole = "1506338573631356958",
    ticketCategory = "1508059255893393499",
    channelPrefix = "суд"

) {
    override fun sendTicketLog(event: ButtonInteractionEvent, dbTicket: Ticket) {
        val jda = event.jda
        val logTextChannel = jda.getTextChannelById(this.logChannel) ?: return

        val field1Val = dbTicket.fields.find { it.fieldLabel == "Court_field_1" }?.fieldValue ?: "Нет данных"
        val field2Val = dbTicket.fields.find { it.fieldLabel == "Court_field_2" }?.fieldValue ?: "Нет данных"
        val field3Val = dbTicket.fields.find { it.fieldLabel == "Court_field_3" }?.fieldValue ?: "Нет данных"
        val field4Val = dbTicket.fields.find { it.fieldLabel == "Court_field_4" }?.fieldValue ?: "Нет данных"

        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
        val closedTime = dbTicket.closedAt?.format(formatter) ?: "Неизвестно"

        val logContainer = Container.of(
            TextDisplay.of("📁 **Архив тикета №${dbTicket.id} [Исковое заявление в Суд]**"),
            Separator.createDivider(Separator.Spacing.LARGE),

            TextDisplay.of("**Информация:**\n• Истец (Автор): <@${dbTicket.authorId}>\n• Закрыл дело: ${event.user.asMention}\n• Время закрытия: $closedTime"),
            Separator.createDivider(Separator.Spacing.LARGE),

            TextDisplay.of("**Причина подачи:**\n$field1Val"),
            Separator.createDivider(Separator.Spacing.LARGE),
            TextDisplay.of("**Описание дела:**\n$field2Val"),
            Separator.createDivider(Separator.Spacing.LARGE),
            TextDisplay.of("**Требования:**\n$field3Val"),
            Separator.createDivider(Separator.Spacing.LARGE),
            TextDisplay.of("**Доказательства:**\n$field4Val")
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
            .setMinLength(15)

            .build()

        val pool2 = TextInput.create(field2.id, field2.style)

            .setPlaceholder(field2.placeholder)
            .setRequired(field2.required)
            .setMinLength(20)

            .build()

        val pool3 = TextInput.create(field3.id, field3.style)

            .setPlaceholder(field3.placeholder)
            .setRequired(field3.required)
            .setMinLength(20)

            .build()

        val pool4 = TextInput.create(field4.id, field4.style)
            .setPlaceholder(field4.placeholder)
            .setRequired(field4.required)
            .setMinLength(20)

            .build()

        val modal = Modal.create("Court_Panel", "⚖️ Подача жалобы в суд")
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

        val field1 = event.getValue("Court_field_1")
            ?.asString ?: "Нет данных"

        val field2 = event.getValue("Court_field_2")
            ?.asString ?: "Нет данных"

        val field3 = event.getValue("Court_field_3")
            ?.asString ?: "Нет данных"

        val field4 = event.getValue("Court_field_4")
            ?.asString ?: "Нет данных"

        val container = Container.of(
            TextDisplay.of("⚖️ Подача жалобы в суд"),
            Separator.createDivider(Separator.Spacing.LARGE),
            TextDisplay.of("**Причина подачи:**\n$field1"),
            Separator.createDivider(Separator.Spacing.LARGE),
            TextDisplay.of("**Описание дела:**\n$field2"),
            Separator.createDivider(Separator.Spacing.LARGE),
            TextDisplay.of("**Требования:**\n$field3"),
            Separator.createDivider(Separator.Spacing.LARGE),
            TextDisplay.of("**Доказательства:**\n$field4")
        )



        val btnContainer = Container.of(
            ActionRow.of(
                closeButton
            )
        )

        return listOf(container, btnContainer)
    }



    val field1 = ModalField(
        label = "Укажите причину подачи",
        id = "Court_field_1",
        placeholder = "Например: убийство, кража, гриф и т.п",
        required = true,
        style = TextInputStyle.SHORT
    )

    val field2 = ModalField(
        label = "Кратко опишите ваше дело",
        id = "Court_field_2",
        placeholder = "Не забудьте указать на кого вы подаёте дело",
        required = true,
        style = TextInputStyle.SHORT
    )
    val field3 = ModalField(
        label = "Укажите требования",
        id = "Court_field_3",
        placeholder = "Важно, чтобы требования были в рамках разумного",
        required = true,
        style = TextInputStyle.PARAGRAPH
    )

    val field4 = ModalField(
        label = "Предоставьте доказательства",
        id = "Court_field_4",
        placeholder = "Запись экрана, скриншоты и т.п (ссылкой)",
        required = true,
        style = TextInputStyle.PARAGRAPH
    )


}