import React from 'react';
import { Task as TaskType } from '../models/types';
import { useDrag } from 'react-dnd';

interface TaskProps {
  task: TaskType;
  onClick: (task: TaskType) => void;
}

const Task: React.FC<TaskProps> = ({ task, onClick }) => {
  const [{ isDragging }, drag] = useDrag(() => ({
    type: 'task',
    item: { id: task.id, status: task.status },
    collect: (monitor) => ({
      isDragging: !!monitor.isDragging(),
    }),
  }));

  return (
    <div
      ref={drag}
      className={`p-3 mb-2 bg-white rounded shadow cursor-pointer hover:shadow-md transition-shadow ${
        isDragging ? 'opacity-50' : 'opacity-100'
      }`}
      onClick={() => onClick(task)}
      style={{ opacity: isDragging ? 0.5 : 1 }}
    >
      <h3 className="font-medium">{task.title}</h3>
      <p className="text-sm text-gray-600 truncate">{task.description}</p>
      <div className="flex justify-between items-center mt-2">
        <span className="text-xs text-gray-500">
          ID: {task.id}
        </span>
        <span className="text-xs bg-blue-100 text-blue-800 px-2 py-1 rounded-full">
          {task.comments.length} comments
        </span>
      </div>
    </div>
  );
};

export default Task;