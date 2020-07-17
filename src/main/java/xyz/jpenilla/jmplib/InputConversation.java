package xyz.jpenilla.jmplib;

import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class InputConversation {
    private BiConsumer<Player, String> acceptedListener;
    private BiConsumer<Player, String> deniedListener;
    private BiPredicate<Player, String> inputValidator;
    private Function<Player, String> promptHandler;
    private BiFunction<Player, String, String> confirmText;

    public InputConversation onAccepted(BiConsumer<Player, String> acceptedListener) {
        this.acceptedListener = acceptedListener;
        return this;
    }

    public InputConversation onDenied(BiConsumer<Player, String> deniedListener) {
        this.deniedListener = deniedListener;
        return this;
    }

    public InputConversation onValidateInput(BiPredicate<Player, String> inputValidator) {
        this.inputValidator = inputValidator;
        return this;
    }

    public InputConversation onPromptText(Function<Player, String> promptHandler) {
        this.promptHandler = promptHandler;
        return this;
    }

    public InputConversation onConfirmText(BiFunction<Player, String, String> confirmTextHandler) {
        this.confirmText = confirmTextHandler;
        return this;
    }

    public void start(Player player) {
        Conversation conversation = BasePlugin.getBasePlugin().getConversationFactory()
                .withFirstPrompt(new StringPrompt() {
                    @Override
                    public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
                        return promptHandler.apply((Player) conversationContext.getForWhom());
                    }

                    @Override
                    public Prompt acceptInput(@NotNull ConversationContext conversationContext, @Nullable String s) {
                        if (!inputValidator.test((Player) conversationContext.getForWhom(), s)) {
                            return this;
                        }
                        if (acceptedListener != null || deniedListener != null) {
                            return new BooleanPrompt() {
                                @Override
                                protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, boolean b) {
                                    if (b && acceptedListener != null) {
                                        acceptedListener.accept((Player) conversationContext.getForWhom(), s);
                                    } else if (deniedListener != null) {
                                        deniedListener.accept((Player) conversationContext.getForWhom(), s);
                                    }
                                    return null;
                                }

                                @Override
                                public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
                                    return confirmText.apply((Player) conversationContext.getForWhom(), s);
                                }
                            };
                        }
                        return null;
                    }
                })
                .withLocalEcho(false)
                .buildConversation(player);
        conversation.begin();
    }
}
