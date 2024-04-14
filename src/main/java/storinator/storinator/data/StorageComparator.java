package storinator.storinator.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;

@RequiredArgsConstructor
@Getter
public enum StorageComparator {
    BY_COUNT(Comparator.comparing(MyItemStack::getCount)),
    BY_COUNT_REVERSED(BY_COUNT.comparator.reversed());

    private final Comparator<MyItemStack> comparator;
}
