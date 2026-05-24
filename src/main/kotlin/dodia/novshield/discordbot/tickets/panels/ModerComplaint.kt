package dodia.novshield.discordbot.tickets.panels

import dev.minn.jda.ktx.interactions.components.ActionRow
import dodia.novshield.discordbot.tickets.Panel
import dodia.novshield.discordbot.tickets.ModalField

import dev.minn.jda.ktx.interactions.components.button
import net.dv8tion.jda.api.components.textinput.TextInputStyle
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.components.container.Container
import net.dv8tion.jda.api.components.textinput.TextInput
import net.dv8tion.jda.api.modals.Modal
import net.dv8tion.jda.api.components.label.Label
import net.dv8tion.jda.api.components.separator.Separator
import net.dv8tion.jda.api.components.textdisplay.TextDisplay
import net.dv8tion.jda.api.components.actionrow.ActionRow
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder
import java.time.format.DateTimeFormatter

import dodia.novshield.discordbot.tickets.database.Ticket





object ModerComplaint : Panel(
    logChannel = "1507440553673883738",
    panelHEX = 0xFF0000,
    supportRole = "1506319039012540487",
    ticketCategory = "1506644887674552410",
    channelPrefix = "модер-жалоба"
) {
    override fun sendTicketLog(event: ButtonInteractionEvent, dbTicket: Ticket) {
        val jda = event.jda

        val logTextChannel = jda.getTextChannelById(this.logChannel) ?: return


        val field1Val = dbTicket.fields.find { it.fieldLabel == "Moder_field_1" }?.fieldValue ?: "Нет данных"
        val field2Val = dbTicket.fields.find { it.fieldLabel == "Moder_field_2" }?.fieldValue ?: "Нет данных"
        val field3Val = dbTicket.fields.find { it.fieldLabel == "Moder_field_3" }?.fieldValue ?: "Нет dados"

        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
        val closedTime = dbTicket.closedAt?.format(formatter) ?: "Неизвестно"

        val logContainer = Container.of(
            TextDisplay.of("📁 **Архив тикета №${dbTicket.id} [Жалоба на модератора]**"),
            Separator.createDivider(Separator.Spacing.LARGE),

            TextDisplay.of("**Информация:**\n• Автор: <@${dbTicket.authorId}>\n• Закрыл: ${event.user.asMention}\n• Время закрытия: $closedTime"),
            Separator.createDivider(Separator.Spacing.LARGE),

            TextDisplay.of("**На кого подана жалоба?:**\n$field1Val"),
            Separator.createDivider(Separator.Spacing.LARGE),
            TextDisplay.of("**Причина:**\n$field2Val"),
            Separator.createDivider(Separator.Spacing.LARGE),
            TextDisplay.of("**Доказательства:**\n$field3Val")
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

        val pool3 = TextInput.create(field3.id, field3.style)
            .setPlaceholder(field3.placeholder)
            .setRequired(field3.required)

            .build()


        val modal = Modal.create("ModerComplaint_panel", "🛡️ Подача жалобы на модератора")
            .addComponents(

                Label.of(field1.label, pool1),
                Label.of(field2.label, pool2),
                Label.of(field3.label, pool3)
            )

            .build()

        event.replyModal(modal).queue()

    }

    override fun buildTicketMessage(event: ModalInteractionEvent): List<Container> {

        val field1 = event.getValue("Moder_field_1")
            ?.asString ?: "Нет данных"

        val field2 = event.getValue("Moder_field_2")
            ?.asString ?: "Нет данных"

        val field3 = event.getValue("Moder_field_3")
            ?.asString ?: "Нет данных"


        val container = Container.of(
            TextDisplay.of("🛡️ Подача жалобы на модератора"),
            Separator.createDivider(Separator.Spacing.LARGE),
            TextDisplay.of("**На кого вы подаёте жалобу?:**\n$field1"),
            Separator.createDivider(Separator.Spacing.LARGE),
            TextDisplay.of("**Причина подачи жалобы:**\n$field2"),
            Separator.createDivider(Separator.Spacing.LARGE),
            TextDisplay.of("**Предоставьте доказательства:**\n$field3"),

            )

        val btnContainer = Container.of(
            ActionRow.of(
                closeButton
            )
        )

        return listOf(container, btnContainer)

    }

    val field1 = ModalField(
        label = "На кого вы подаёте жалобу?",
        id = "Moder_field_1",
        placeholder = "Укажите никнейм(ы) модератора(ов)",
        required = true,
        style = TextInputStyle.SHORT
    )

    val field2 = ModalField(
        label = "Причина подачи жалобы",
        id = "Moder_field_2",
        placeholder = "Желательно указать коротко и по существу",
        required = true,
        style = TextInputStyle.SHORT
    )
    val field3 = ModalField(
        label = "Предоставьте доказательства",
        id = "Moder_field_3",
        placeholder = "Скриншоты, запись экрана и т.д (ссылкой)",
        required = true,
        style = TextInputStyle.PARAGRAPH
    )

}