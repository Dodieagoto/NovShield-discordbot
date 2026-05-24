package dodia.novshield.discordbot.tickets.listeners.utils


import net.dv8tion.jda.api.components.container.Container
import dev.minn.jda.ktx.interactions.components.button
import net.dv8tion.jda.api.components.actionrow.ActionRow
import net.dv8tion.jda.api.components.buttons.ButtonStyle
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.components.textdisplay.TextDisplay
import net.dv8tion.jda.api.components.separator.Separator
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder
import java.awt.Color
import java.util.concurrent.TimeUnit

import org.jetbrains.exposed.sql.transactions.transaction
import dodia.novshield.discordbot.tickets.database.Ticket
import dodia.novshield.discordbot.tickets.database.Tickets

fun ticketDeleteInteraction(event: ButtonInteractionEvent) {

    val applyDeleteButton = button(
        customId = "btn_apply_delete",
        label = "Подтвердить",
        style = ButtonStyle.SUCCESS,

    )

    val applyDeleteContainer = Container.of(

        TextDisplay.of(
            "> ## :warning: Вы уверены, что хотите удалить тикет?"
        ),
        Separator.createDivider(Separator.Spacing.LARGE),

        ActionRow.of(
            applyDeleteButton,
        )



    ).withAccentColor(Color(0xFF8C00).rgb)

    val message = MessageCreateBuilder()
        .useComponentsV2()
        .setComponents(applyDeleteContainer)
        .build()

    event.reply(message)
        .setEphemeral(true)
        .queue()

}

fun ticketDeleteApply(event: ButtonInteractionEvent) {
    event.message.delete().queue()

    val channelId = event.channel.idLong


    transaction {
        val dbTicket = Ticket.find { Tickets.channelId eq channelId }.firstOrNull()

        if (dbTicket != null) {
            dbTicket.delete()
            println("Тикет для канала $channelId успешно удален из БД.")
        } else {
            println("Предупреждение: тикет с каналом $channelId не найден в БД при удалении.")
        }
    }

    event.channel.sendMessage("Тикет будет удалён через 5 секунд...").queue()

    event.channel.delete().queueAfter(5, TimeUnit.SECONDS)
}