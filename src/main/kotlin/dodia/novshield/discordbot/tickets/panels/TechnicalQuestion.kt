package dodia.novshield.discordbot.tickets.panels


import dodia.novshield.discordbot.tickets.Panel
import dodia.novshield.discordbot.tickets.ModalField

import dev.minn.jda.ktx.interactions.components.Separator
import net.dv8tion.jda.api.components.actionrow.ActionRow
import net.dv8tion.jda.api.components.textinput.TextInputStyle
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.components.container.Container
import net.dv8tion.jda.api.components.textinput.TextInput
import net.dv8tion.jda.api.modals.Modal
import net.dv8tion.jda.api.components.label.Label
import net.dv8tion.jda.api.components.textdisplay.TextDisplay
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder
import java.time.format.DateTimeFormatter

import dodia.novshield.discordbot.tickets.database.Ticket
import net.dv8tion.jda.api.components.separator.Separator

object TechnicalQuestion : Panel(
    logChannel = "1507448073155121242",
    panelHEX = 0x7557A5,
    supportRole = "1506319265400225803",
    ticketCategory = "1508060340024381440",
    channelPrefix = "тех-вопрос"
){
    override fun sendTicketLog(event: ButtonInteractionEvent, dbTicket: Ticket) {
        val jda = event.jda
        val logTextChannel = jda.getTextChannelById(this.logChannel) ?: return

        val field1Val = dbTicket.fields.find { it.fieldLabel == "Tech_field_1" }?.fieldValue ?: "Нет данных"
        val field2Val = dbTicket.fields.find { it.fieldLabel == "Tech_field_2" }?.fieldValue ?: "Нет данных"

        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
        val closedTime = dbTicket.closedAt?.format(formatter) ?: "Неизвестно"

        val logContainer = Container.of(
            TextDisplay.of("📁 **Архив тикета №${dbTicket.id} [Технический вопрос]**"),
            net.dv8tion.jda.api.components.separator.Separator.createDivider(net.dv8tion.jda.api.components.separator.Separator.Spacing.LARGE),

            TextDisplay.of("**Информация:**\n• Автор: <@${dbTicket.authorId}>\n• Закрыл: ${event.user.asMention}\n• Время закрытия: $closedTime"),
            net.dv8tion.jda.api.components.separator.Separator.createDivider(net.dv8tion.jda.api.components.separator.Separator.Spacing.LARGE),

            TextDisplay.of("**Никнейм:**\n$field1Val"),
            net.dv8tion.jda.api.components.separator.Separator.createDivider(net.dv8tion.jda.api.components.separator.Separator.Spacing.LARGE),
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

        val modal = Modal.create("TechnicalQuestion_panel", "🛠️ Подача технического вопроса")
            .addComponents(
                Label.of(field1.label, pool1),
                Label.of(field2.label, pool2)
            )

            .build()
        event.replyModal(modal).queue()
    }

    override fun buildTicketMessage(event: ModalInteractionEvent): List<Container> {

        val pool1 = event.getValue("Tech_field_1")
            ?.asString ?: "Нет данных"

        val pool2 = event.getValue("Tech_field_2")
            ?.asString ?: "Нет данных"


        val upContent = Container.of(
            TextDisplay.of(

                "> ## 🛠️ Технический вопрос <@&$supportRole> \n\n" +
                        "> ${event.user.asMention} Ваш вопрос подан на рассмотрение! Тех. Админ ответит вам в тикете в течении **12 часов** "

            ),

            Separator.createDivider(Separator.Spacing.LARGE),


            TextDisplay.of(
                "> :warning: Не пингуйте администрацию или ответственных людей в тикетах. Мы видим и помним про каждый созданный тикет"

            )

        ).withAccentColor(panelHEX)

        val answersContainer = Container.of(

            TextDisplay.of(
                "1. Ваш никнейм \n```$pool1                      ``` \n" +
                        "2. Напишите свой вопрос \n```$pool2  ``` \n"
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
