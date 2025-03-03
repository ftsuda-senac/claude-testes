import React from 'react';
import { useDrop } from 'react-dnd';
import { Task as TaskType, ColumnType, TaskStatus } from '../models/types';
import Task from './Task';
import { updateTaskStatus } from '../services/api';

interface ColumnProps {
  column: ColumnType;
  tasks: TaskType[];
  onTaskClick: (task: TaskType) => void;
  onTaskUpdate: () => void;
}

const Column: React.FC<ColumnProps> = ({ column, tasks, onTaskClick, onTaskUpdate }) => {
  const [{ isOver }, drop] = useDrop(() => ({
    accept: 'task',
    drop: (item: { id: number, status: TaskStatus }) => {
      if (item.status !== column.status) {
        handleTaskDrop(item.id);
      }
    },
    collect: (monitor) => ({
      isOver: !!monitor.isOver(),
    }),
  }));

  const handleTaskDrop = async (taskId: number) => {
    try {
      await updateTaskStatus(taskId, column.status);
      onTaskUpdate();
    } catch (error) {
      console.error('Error updating task status:', error);
    }
  };

  return (
    <div 
      ref={drop} 
      className={`bg-gray-100 p-3 rounded-lg flex-1 min-h-96 ${isOver ? 'bg-gray-200' : ''}`}
    >
      <h2 className="text-lg font-semibold mb-4 pb-2 border-b border-gray-300">{column.title}</h2>
      <div>
        {tasks.filter(task => task.status === column.status).map(task => (
          <Task key={task.id} task={task} onClick={onTaskClick} />
        ))}
      </div>
    </div>
  );
};

export default Column;