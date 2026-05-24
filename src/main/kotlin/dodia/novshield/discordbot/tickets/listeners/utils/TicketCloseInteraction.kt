package dodia.novshield.discordbot.tickets.listeners.utils

import dev.minn.jda.ktx.interactions.components.button
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.components.actionrow.ActionRow
import net.dv8tion.jda.api.components.buttons.ButtonStyle
import net.dv8tion.jda.api.components.container.Container
import net.dv8tion.jda.api.components.separator.Separator
import net.dv8tion.jda.api.components.textdisplay.TextDisplay
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder
import org.jetbrains.exposed.sql.transactions.transaction
import java.awt.Color
import java.time.LocalDateTime

import dodia.novshield.discordbot.tickets.database.Ticket
import dodia.novshield.discordbot.tickets.database.Tickets

fun ticketCloseInteraction(event: ButtonInteractionEvent) {
    val applyButton = button(
        customId = "btn_apply_close",
        label = "Подтвердить",
        style = ButtonStyle.SUCCESS,
    )

    val container = Container.of(
        TextDisplay.of("> ## :warning: Вы уверены, что хотите закрыть тикет?"),
        Separator.createDivider(Separator.Spacing.LARGE),
        ActionRow.of(applyButton)
    ).withAccentColor(Color(0xFF8C00).rgb)

    val message = MessageCreateBuilder()
        .useComponentsV2()
        .setComponents(container)
        .build()

    event.reply(message)
        .setEphemeral(true)
        .queue()
}

fun ticketCloseApply(event: ButtonInteractionEvent) {
    event.message.delete().queue()



    val channel = event.channel
    val textChannel = channel.asTextChannel()
    val statusText = "Тикет закрыт игроком ${event.user.asMention}"
    var ticketPanel: String? = null
    var authorId: Long? = null

    transaction {
        val dbTicket = Ticket.find { Tickets.channelId eq channel.idLong }.firstOrNull()
        if (dbTicket != null) {
            dbTicket.status = "CLOSED"
            dbTicket.closedAt = LocalDateTime.now()
            authorId = dbTicket.authorId
            ticketPanel = dbTicket.ticketType
        }
    }

    if (authorId != null) {
        textChannel.getManager()
            .setName("закрыт")
            .putMemberPermissionOverride(
                authorId!!,
                null,
                listOf(Permission.VIEW_CHANNEL)
            )
            .queue()
    }

    val container = Container.of(
        TextDisplay.of(statusText),
    ).withAccentColor(Color(0x4682B4).rgb)

    val message = MessageCreateBuilder()
        .useComponentsV2()
        .setComponents(container)
        .build()

    textChannel.sendMessage(message).queue()

    val deleteButton = button(
        customId = "btn_delete",
        label = "Удалить",
        style = ButtonStyle.DANGER,
    )

    val logButton = button(
        customId = "btn_log",
        label = "💾",
        style = ButtonStyle.PRIMARY,

    )

    val adminContainer = Container.of(
        TextDisplay.of(
            "> ## ❌ Тикет закрыт \n\n"
                    + "-# Автор: $authorId \n"
                    + "-# Панель: $ticketPanel \n"
        ),

        Separator.createDivider(Separator.Spacing.LARGE),

        ActionRow.of(
            deleteButton,
            logButton,
        )

    )

    val adminMessage = MessageCreateBuilder()
        .useComponentsV2()
        .setComponents(adminContainer)
        .build()

    textChannel.sendMessage(adminMessage).queue()

}