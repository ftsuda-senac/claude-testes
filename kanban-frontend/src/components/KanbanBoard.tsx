import React, { useState, useEffect } from 'react';
import { DndProvider } from 'react-dnd';
import { HTML5Backend } from 'react-dnd-html5-backend';
import Column from './Column';
import TaskDetails from './TaskDetails';
import AddTaskForm from './AddTaskForm';
import { Task, TaskStatus, ColumnType } from '../models/types';
import { fetchAllTasks } from '../services/api';

const KanbanBoard: React.FC = () => {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [selectedTaskId, setSelectedTaskId] = useState<number | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [showAddTask, setShowAddTask] = useState(false);

  const columns: ColumnType[] = [
    { title: 'Backlog', status: TaskStatus.BACKLOG },
    { title: 'In Progress', status: TaskStatus.IN_PROGRESS },
    { title: 'Finished', status: TaskStatus.FINISHED }
  ];

  const loadTasks = async () => {
    try {
      setIsLoading(true);
      const tasksData = await fetchAllTasks();
      setTasks(tasksData);
    } catch (error) {
      console.error('Error loading tasks:', error);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    loadTasks();
  }, []);

  const handleTaskClick = (task: Task) => {
    setSelectedTaskId(task.id);
  };

  const handleCloseTaskDetails = () => {
    setSelectedTaskId(null);
  };

  const handleTaskAdded = () => {
    loadTasks();
    setShowAddTask(false);
  };

  return (
    <DndProvider backend={HTML5Backend}>
      <div className="container mx-auto p-4 max-w-7xl">
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-3xl font-bold text-gray-800">Kanban Board</h1>
          <button
            onClick={() => setShowAddTask(true)}
            className="px-4 py-2 bg-green-500 text-white rounded-md hover:bg-green-600 flex items-center"
          >
            <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 4v16m8-8H4"></path>
            </svg>
            Add Task
          </button>
        </div>

        {isLoading ? (
          <div className="text-center py-10">Loading tasks...</div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            {columns.map(column => (
              <Column
                key={column.status}
                column={column}
                tasks={tasks}
                onTaskClick={handleTaskClick}
                onTaskUpdate={loadTasks}
              />
            ))}
          </div>
        )}

        {selectedTaskId && (
          <TaskDetails
            taskId={selectedTaskId}
            onClose={handleCloseTaskDetails}
            onTaskUpdated={loadTasks}
          />
        )}

        {showAddTask && (
          <AddTaskForm
            onTaskAdded={handleTaskAdded}
            onCancel={() => setShowAddTask(false)}
          />
        )}
      </div>
    </DndProvider>
  );
};

export default KanbanBoard;