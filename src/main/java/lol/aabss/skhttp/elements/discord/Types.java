package lol.aabss.skhttp.elements.discord;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import com.itsradiix.discordwebhook.DiscordWebHook;
import com.itsradiix.discordwebhook.embed.Embed;
import com.itsradiix.discordwebhook.embed.models.Author;
import com.itsradiix.discordwebhook.embed.models.Field;
import com.itsradiix.discordwebhook.embed.models.Footer;
import org.jetbrains.annotations.NotNull;

public class Types {
    static {
        Classes.registerClass(new ClassInfo<>(DiscordWebHook.class, "discordwebhook")
                .name("Discord Webhook")
                .description("Represents a discord webhook.")
                .user("discord ?webhooks?")
                .since("1.1")
                .parser(new Parser<>() {

                            @Override
                            public boolean canParse(@NotNull ParseContext context) {
                                return false;
                            }

                            @Override
                            public @NotNull String toString(DiscordWebHook o, int flags) {
                                return toVariableNameString(o);
                            }

                            @Override
                            public @NotNull String toVariableNameString(DiscordWebHook o) {
                                return o.getContent();
                            }
                        }
                )
        );

        Classes.registerClass(new ClassInfo<>(Embed.class, "discordembed")
                .name("Discord Embed")
                .description("Represents a discord embed.")
                .user("discord ?embeds?")
                .since("1.1")
                .parser(new Parser<>() {

                            @Override
                            public boolean canParse(@NotNull ParseContext context) {
                                return false;
                            }

                            @Override
                            public @NotNull String toString(Embed o, int flags) {
                                return toVariableNameString(o);
                            }

                            @Override
                            public @NotNull String toVariableNameString(Embed o) {
                                return o.getTitle();
                            }
                        }
                )
        );

        Classes.registerClass(new ClassInfo<>(Footer.class, "discordfooter")
                .name("Discord Footer")
                .description("Represents a discord embed footer.")
                .user("discord ?footers?")
                .since("1.1")
                .parser(new Parser<>() {

                            @Override
                            public boolean canParse(@NotNull ParseContext context) {
                                return false;
                            }

                            @Override
                            public @NotNull String toString(Footer o, int flags) {
                                return toVariableNameString(o);
                            }

                            @Override
                            public @NotNull String toVariableNameString(Footer o) {
                                return o.getText();
                            }
                        }
                )
        );

        Classes.registerClass(new ClassInfo<>(Author.class, "discordauthor")
                .name("Discord Author")
                .description("Represents a discord embed author.")
                .user("discord ?authors?")
                .since("1.1")
                .parser(new Parser<>() {

                            @Override
                            public boolean canParse(@NotNull ParseContext context) {
                                return false;
                            }

                            @Override
                            public @NotNull String toString(Author o, int flags) {
                                return toVariableNameString(o);
                            }

                            @Override
                            public @NotNull String toVariableNameString(Author o) {
                                return o.getName();
                            }
                        }
                )
        );

        Classes.registerClass(new ClassInfo<>(Field.class, "discordfield")
                .name("Discord Field")
                .description("Represents a discord embed field.")
                .user("discord ?fields?")
                .since("1.1")
                .parser(new Parser<>() {

                            @Override
                            public boolean canParse(@NotNull ParseContext context) {
                                return false;
                            }

                            @Override
                            public @NotNull String toString(Field o, int flags) {
                                return toVariableNameString(o);
                            }

                            @Override
                            public @NotNull String toVariableNameString(Field o) {
                                return o.getName();
                            }
                        }
                )
        );
    }
}
