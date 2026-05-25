package dodia.novshield.discordbot.tickets.panels

import dodia.novshield.discordbot.tickets.Panel
import dodia.novshield.discordbot.tickets.ModalField

import net.dv8tion.jda.api.components.textinput.TextInputStyle
import dev.minn.jda.ktx.interactions.components.Separator
import net.dv8tion.jda.api.components.actionrow.ActionRow
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.components.container.Container
import net.dv8tion.jda.api.components.textinput.TextInput
import net.dv8tion.jda.api.modals.Modal
import net.dv8tion.jda.api.components.label.Label
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.components.textdisplay.TextDisplay

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder
import java.time.format.DateTimeFormatter

import dodia.novshield.discordbot.tickets.database.Ticket
import net.dv8tion.jda.api.components.separator.Separator


object TerritoryReg : Panel(
    logChannel = "1507440553673883738",
    panelHEX = 0xFF0000,
    supportRole = "1506338573631356958",
    ticketCategory = "1508061743749009519",
    channelPrefix = "территория"
){
    override fun sendTicketLog(event: ButtonInteractionEvent, dbTicket: Ticket) {
        val jda = event.jda
        val logTextChannel = jda.getTextChannelById(this.logChannel) ?: return

        val field1Val = dbTicket.fields.find { it.fieldLabel == "Territory_field_1" }?.fieldValue ?: "Нет данных"
        val field2Val = dbTicket.fields.find { it.fieldLabel == "Territory_field_2" }?.fieldValue ?: "Нет данных"
        val field3Val = dbTicket.fields.find { it.fieldLabel == "Territory_field_3" }?.fieldValue ?: "Нет данных"

        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
        val closedTime = dbTicket.closedAt?.format(formatter) ?: "Неизвестно"

        val logContainer = Container.of(
            TextDisplay.of("📁 **Архив тикета №${dbTicket.id} [Регистрация территории]**"),
            net.dv8tion.jda.api.components.separator.Separator.createDivider(net.dv8tion.jda.api.components.separator.Separator.Spacing.LARGE),

            TextDisplay.of("**Информация:**\n• Автор: <@${dbTicket.authorId}>\n• Закрыл: ${event.user.asMention}\n• Время закрытия: $closedTime"),
            net.dv8tion.jda.api.components.separator.Separator.createDivider(net.dv8tion.jda.api.components.separator.Separator.Spacing.LARGE),

            TextDisplay.of("**Координаты территории:**\n$field1Val"),
            net.dv8tion.jda.api.components.separator.Separator.createDivider(net.dv8tion.jda.api.components.separator.Separator.Spacing.LARGE),
            TextDisplay.of("**Границы территории:**\n$field2Val"),
            net.dv8tion.jda.api.components.separator.Separator.createDivider(net.dv8tion.jda.api.components.separator.Separator.Spacing.LARGE),
            TextDisplay.of("**Владельцы территории:**\n$field3Val")
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

        val modal = Modal.create("Territory_panel", "🗺️ Регистрация территории")
            .addComponents(
                Label.of(field1.label, pool1),
                Label.of(field2.label, pool2),
                Label.of(field3.label, pool3),

            )

            .build()

        event.replyModal(modal).queue()
    }

        override fun buildTicketMessage(event: ModalInteractionEvent): List<Container> {
            val pool1 = event.getValue("Territory_field_1")
                ?.asString ?: "Нет данных"

            val pool2 = event.getValue("Territory_field_2")
                ?.asString ?: "Нет данных"

            val pool3 = event.getValue("Territory_field_3")
                ?.asString ?: "Нет данных"

            val upContent = Container.of(
                TextDisplay.of(

                    "> ## 🗺️ Регистрация территории <@&$supportRole> \n\n" +
                            "> ${event.user.asMention} Ваша заявка подана! Тех. Админ ответит вам в тикете в течении **12 часов** "

                ),

                Separator.createDivider(Separator.Spacing.LARGE),


                TextDisplay.of(
                    "> :warning: Не пингуйте администрацию или ответственных людей в тикетах. Мы видим и помним про каждый созданный тикет"

                )

            ).withAccentColor(panelHEX)

            val answersContainer = Container.of(

                TextDisplay.of(
                    "1. Укажите координаты территории \n```$pool1                      ``` \n" +
                            "2. Укажите границы вашей территории \n```$pool2  ``` \n" +
                            "3. Напишите никнейм(ы) владельца(ов) территории \n```$pool3 ``` \n "
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