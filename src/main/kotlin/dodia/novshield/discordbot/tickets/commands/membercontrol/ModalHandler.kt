package dodia.novshield.discordbot.tickets.commands.membercontrol

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.entities.channel.attribute.IPermissionContainer

import net.dv8tion.jda.api.components.container.Container
import java.awt.Color
import net.dv8tion.jda.api.components.textdisplay.TextDisplay
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder

object ModalHandler {

    fun onModalHandler(event: ModalInteractionEvent, guild: Guild) {

        val modalMember = event.getValue("role-select-members")?.getAsStringList()
        val modalAction = event.getValue("action-select")?.getAsStringList()

        val user : String = modalMember?.first()!!
        val action = modalAction?.first()
        val member = guild.getMemberById(user)

        val permissionContainer = event.channel as? IPermissionContainer

        when (action) {

            "SelectMenu_Action_Add" -> {

                permissionContainer?.upsertPermissionOverride(member!!)
                    ?.setAllowed(
                        Permission.VIEW_CHANNEL,
                        Permission.MESSAGE_SEND,
                        Permission.MESSAGE_HISTORY,
                        Permission.MESSAGE_ATTACH_FILES
                    )
                    ?.queue()

                val container = Container.of(
                    TextDisplay.of(
                        " ## 🟢 ${event.user.asMention} добавил <@${user}> в тикет"
                    )
                ).withAccentColor(Color(0x60FF7A).rgb)

                val message = MessageCreateBuilder()
                    .useComponentsV2()
                    .setComponents(container)
                    .build()

                event.channel.sendMessage(message).queue()

            }

            "SelectMenu_Action_Remove" -> {

                permissionContainer?.upsertPermissionOverride(member!!)
                    ?.setDenied(
                        Permission.VIEW_CHANNEL,
                        Permission.MESSAGE_SEND,
                        Permission.MESSAGE_HISTORY,
                        Permission.MESSAGE_ATTACH_FILES
                    )
                ?.queue()


                val container = Container.of(
                    TextDisplay.of(
                        " ## 🔴 ${event.user.asMention} удалил <@${user}> из тикета"
                    )
                ).withAccentColor(Color(0xFF6060).rgb)

                val message = MessageCreateBuilder()
                    .useComponentsV2()
                    .setComponents(container)
                    .build()

                event.channel.sendMessage(message).queue()

            }

        }

    }

}