package org.north.core.architecture.tree;

import org.north.core.architecture.entity.Entity;
import org.north.core.component.Component;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface Tree<N extends TreeNode<N>> extends Collection<N> {
    N create();
    N create(String name);
    N create(N parent);
    N create(N parent, String name);

    N getRoot();
    N getById(UUID id);
    N getByIdFromParent(N parent, UUID id);
    N getByName(String name);
    N getByNameFromParent(N parent, String name);
    List<N> getByComponents(Class<? extends Component>... componentTypes);
    List<N> getByComponentsFromParent(List<N> entities, N parent, Class<? extends Component>... componentTypes);


}
