package ecs.systems;

import ecs.systems.processes.IRender;
import ecs.systems.processes.IStart;
import ecs.systems.processes.IUpdate;

public interface ISystem
        extends IStart, IUpdate, IRender {

}
