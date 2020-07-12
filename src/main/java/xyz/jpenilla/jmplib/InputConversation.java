package xyz.jpenilla.jmplib;

import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class InputConversation {
    private final ConversationFactory conversationFactory;
    private Consumer<String> acceptedListener;
    private Consumer<String> deniedListener;
    private Predicate<String> inputValidator;
    private Function<ConversationContext, String> promptHandler;
    private BiFunction<ConversationContext, String, String> confirmText;

    public InputConversation(ConversationFactory conversationFactory) {
        this.conversationFactory = conversationFactory;
    }

    public InputConversation onAccepted(Consumer<String> acceptedListener) {
        this.acceptedListener = acceptedListener;
        return this;
    }

    public InputConversation onDenied(Consumer<String> deniedListener) {
        this.deniedListener = deniedListener;
        return this;
    }

    public InputConversation onValidateInput(Predicate<String> inputValidator) {
        this.inputValidator = inputValidator;
        return this;
    }

    public InputConversation onPromptText(Function<ConversationContext, String> promptHandler) {
        this.promptHandler = promptHandler;
        return this;
    }

    public InputConversation onConfirmText(BiFunction<ConversationContext, String, String> confirmTextHandler) {
        this.confirmText = confirmTextHandler;
        return this;
    }

    public void start(Player player) {
        Conversation conversation = conversationFactory
                .withFirstPrompt(new StringPrompt() {
                    @Override
                    public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
                        return promptHandler.apply(conversationContext);
                    }

                    @Override
                    public Prompt acceptInput(@NotNull ConversationContext conversationContext, @Nullable String s) {
                        if (!inputValidator.test(s)) {
                            return this;
                        }
                        if (acceptedListener != null || deniedListener != null) {
                            return new BooleanPrompt() {
                                @Override
                                protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, boolean b) {
                                    if (b && acceptedListener != null) {
                                        acceptedListener.accept(s);
                                    } else if (deniedListener != null) {
                                        deniedListener.accept(s);
                                    }
                                    return null;
                                }

                                @Override
                                public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
                                    return confirmText.apply(conversationContext, s);
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
