/*
    Используется в пакете panels для создания панелей

 */

package dodia.novshield.discordbot.tickets

import dev.minn.jda.ktx.interactions.components.button
import net.dv8tion.jda.api.Permission

import net.dv8tion.jda.api.components.actionrow.ActionRow
import dev.minn.jda.ktx.interactions.components.button
import net.dv8tion.jda.api.components.buttons.ButtonStyle
import net.dv8tion.jda.api.components.container.Container
import net.dv8tion.jda.api.components.textinput.TextInputStyle
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder
import java.awt.Color
import net.dv8tion.jda.api.entities.emoji.Emoji

import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import dodia.novshield.discordbot.tickets.database.Ticket
import dodia.novshield.discordbot.tickets.database.TicketField

data class ModalField(
    val id: String,
    val label: String,
    val placeholder: String,
    val required: Boolean = true,
    val style: TextInputStyle = TextInputStyle.SHORT,
)

abstract class Panel(

    val logChannel : String,
    val panelHEX : Int,
    val supportRole : String,
    val ticketCategory: String,
    val channelPrefix: String,

){

    val closeButton = button(
        label = "Закрыть",
        style = ButtonStyle.DANGER,

        customId = "btn_close",
        emoji = Emoji.fromUnicode("❌")

    )

    abstract fun sendTicketLog()
    abstract fun showModal(event: StringSelectInteractionEvent)
    abstract fun buildTicketMessage(event: ModalInteractionEvent): List<Container>

    fun handleModalSubmit(event: ModalInteractionEvent) {
        val guild = event.guild ?: return
        val member = event.member ?: return
        val category = guild.getCategoryById(ticketCategory)
        val support = guild.getRoleById(supportRole)

        val containers = buildTicketMessage(event)
        val message = MessageCreateBuilder()
            .useComponentsV2()
            .setComponents(containers)
            .build()

        val channelName = "$channelPrefix-${event.user.name}"


        event.deferReply(true).queue()

        guild.createTextChannel(channelName)
            .setParent(category)
            .addRolePermissionOverride(guild.publicRole.idLong, null, listOf(Permission.VIEW_CHANNEL))
            .addMemberPermissionOverride(
                member.idLong,
                listOf(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND, Permission.MESSAGE_ATTACH_FILES),
                null
            )
            .apply {
                if (support != null) {
                    addRolePermissionOverride(
                        support.idLong,
                        listOf(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND, Permission.MESSAGE_ATTACH_FILES),
                        null
                    )
                }
            }
            .flatMap { channel -> channel.sendMessage(message) }
            .queue(
                { successMessage ->

                    val channel = successMessage.channel


                    transaction {

                        val newTicket = Ticket.new {
                            this.channelId = channel.idLong
                            this.authorId = member.idLong
                            this.ticketType = channelPrefix
                            this.createdAt = LocalDateTime.now()
                        }


                        event.values.forEach { fieldValue ->
                            TicketField.new {
                                this.ticket = newTicket
                                this.fieldLabel = fieldValue.customId
                                this.fieldValue = fieldValue.asString
                            }
                        }
                    }



                    event.hook.sendMessage("Ваш тикет успешно создан: ${successMessage.channel.asMention}")
                        .setEphemeral(true)
                        .queue()
                },
                { error ->
                    event.hook.sendMessage("Ошибка при создании тикета: ${error.message}")
                        .setEphemeral(true)
                        .queue()
                }
            )
    }

    }

