package dodia.novshield.discordbot.tickets.commands

import dev.minn.jda.ktx.events.onCommand
import net.dv8tion.jda.api.JDA

import dev.minn.jda.ktx.jdabuilder.light
import dev.minn.jda.ktx.events.listener
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.requests.GatewayIntent
import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.interactions.components.MediaGalleryItem
import net.dv8tion.jda.api.components.container.Container
import java.awt.Color
import net.dv8tion.jda.api.components.textdisplay.TextDisplay
import net.dv8tion.jda.api.components.separator.Separator
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.components.actionrow.ActionRow
import net.dv8tion.jda.api.components.label.Label
import net.dv8tion.jda.api.components.mediagallery.MediaGallery
import net.dv8tion.jda.api.components.selections.EntitySelectMenu
import net.dv8tion.jda.api.components.selections.StringSelectMenu
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.modals.Modal

object CommandsHandler {

    private val ALLOWED_ROLES = setOf(
        "1506338573631356958",
        "1506319039012540487",
        "1506319265400225803",
        "1506335312295497758",
        "1506319765730234379",
    )

    fun onCommand(jda: JDA) {

        jda.onCommand("управление_участниками") { event ->
            val member = event.member ?: return@onCommand

            val hasRole = member.roles.any { it.id in ALLOWED_ROLES}

            if (!hasRole) {

                val container = Container.of(
                    TextDisplay.of("❌ У тебя нет прав на использование этой команды!")
                ).withAccentColor(Color(0xFF535B).rgb)

                val message = MessageCreateBuilder()
                    .useComponentsV2()
                    .setComponents(container)
                    .build()

                event.reply(message)
                    .setEphemeral(true)
                    .queue()
                return@onCommand
            }

            val modal = Modal.create("modal-user-action", "🛡 Управление участниками тикета")
                .addComponents(
                    TextDisplay.of("""
            ## Управление участниками
           
            - Выберите участника и действие над ним
            
            """),
                    Label.of(
                        "Участник",
                        EntitySelectMenu.create("role-select-members", EntitySelectMenu.SelectTarget.USER).build()
                    ),

                    Label.of(
                        "Действие",
                        StringSelectMenu.create("action-select")
                            .addOption(
                                "Добавить в тикет",
                                "SelectMenu_Action_Add",
                                "Добавить участника в тикет",
                                Emoji.fromUnicode("🟢")
                            )

                            .addOption(
                                "Удалить из тикета",
                                "SelectMenu_Action_Remove",
                                "Удалить участника из тикета",
                                Emoji.fromUnicode("🔴")
                            )
                            .build(),
                    )
                )
                .build()

            event.replyModal(modal).queue()
        }

    }

}