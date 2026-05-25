package dodia.novshield.discordbot.tickets.panels

import dodia.novshield.discordbot.tickets.Panel
import dodia.novshield.discordbot.tickets.ModalField

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
    panelHEX = 0xE5644F,
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

        val pool1 = event.getValue("Moder_field_1")
            ?.asString ?: "Нет данных"

        val pool2 = event.getValue("Moder_field_2")
            ?.asString ?: "Нет данных"

        val pool3 = event.getValue("Moder_field_3")
            ?.asString ?: "Нет данных"


        val upContent = Container.of(
            TextDisplay.of(

                "> ## 🛡️ Жалоба на модератора <@&$supportRole> \n\n" +
                        "> ${event.user.asMention} Ваша жалоба подана на рассмотрение! Тех. Админ ответит вам в тикете в течении **12 часов** "

            ),

            Separator.createDivider(Separator.Spacing.LARGE),


            TextDisplay.of(
                "> :warning: Не пингуйте администрацию или ответственных людей в тикетах. Мы видим и помним про каждый созданный тикет"

            )

        ).withAccentColor(panelHEX)

        val answersContainer = Container.of(

            TextDisplay.of(
                "1. На кого вы подаёте жалобу? \n```$pool1                      ``` \n" +
                        "2. Причина подачи жалобы \n```$pool2  ``` \n" +
                        "3. Предоставьте доказательства \n```$pool3 ``` \n "
            )
        )

        val btnContainer = Container.of(
            ActionRow.of(
                closeButton
            )
        )

        return listOf(upContent, answersContainer, btnContainer)

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