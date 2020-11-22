package ua.tpetrenko.esp.core.components;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import ua.tpetrenko.esp.api.handlers.ItemHandler;

@RequiredArgsConstructor
public abstract class AbstractHandler<D> implements ItemHandler<D> {
    protected final PlatformTransactionManager transactionManager;

    protected abstract void doHandle(D itemDto);

    @Override
    public void handle(D itemDto) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.executeWithoutResult(transactionStatus -> doHandle(itemDto));
    }
}
