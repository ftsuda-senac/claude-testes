import React, { useEffect, useState } from 'react';
import { Task, TaskStatus } from '../models/types';
import CommentList from './CommentList';
import AddCommentForm from './AddCommentForm';
import { fetchTaskById, updateTask, deleteTask, fetchCommentsByTaskId } from '../services/api';

interface TaskDetailsProps {
  taskId: number;
  onClose: () => void;
  onTaskUpdated: () => void;
}

const TaskDetails: React.FC<TaskDetailsProps> = ({ taskId, onClose, onTaskUpdated }) => {
  const [task, setTask] = useState<Task | null>(null);
  const [isEditing, setIsEditing] = useState(false);
  const [editTitle, setEditTitle] = useState('');
  const [editDescription, setEditDescription] = useState('');
  const [editStatus, setEditStatus] = useState<TaskStatus>(TaskStatus.BACKLOG);
  const [isLoading, setIsLoading] = useState(true);
  const [isSubmitting, setIsSubmitting] = useState(false);

  useEffect(() => {
    const loadTask = async () => {
      try {
        setIsLoading(true);
        const taskData = await fetchTaskById(taskId);
        const commentsData = await fetchCommentsByTaskId(taskId);
        setTask({ ...taskData, comments: commentsData });
        setEditTitle(taskData.title);
        setEditDescription(taskData.description || '');
        setEditStatus(taskData.status);
      } catch (error) {
        console.error('Error loading task details:', error);
      } finally {
        setIsLoading(false);
      }
    };

    loadTask();
  }, [taskId]);

  const handleCommentAdded = async () => {
    if (task) {
      const commentsData = await fetchCommentsByTaskId(task.id);
      setTask({ ...task, comments: commentsData });
    }
  };

  const handleSaveChanges = async () => {
    if (!task) return;

    try {
      setIsSubmitting(true);
      await updateTask(task.id, {
        title: editTitle,
        description: editDescription,
        status: editStatus
      });
      
      // Refresh task data
      const updatedTask = await fetchTaskById(task.id);
      const commentsData = await fetchCommentsByTaskId(task.id);
      setTask({ ...updatedTask, comments: commentsData });
      
      setIsEditing(false);
      onTaskUpdated();
    } catch (error) {
      console.error('Error updating task:', error);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleDeleteTask = async () => {
    if (!task || !window.confirm('Are you sure you want to delete this task?')) return;

    try {
      await deleteTask(task.id);
      onClose();
      onTaskUpdated();
    } catch (error) {
      console.error('Error deleting task:', error);
    }
  };

  if (isLoading) {
    return (
      <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-10">
        <div className="bg-white rounded-lg p-6 w-full max-w-2xl h-4/5 flex flex-col">
          <div className="text-center py-8">Loading task details...</div>
        </div>
      </div>
    );
  }

  if (!task) {
    return (
      <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-10">
        <div className="bg-white rounded-lg p-6 w-full max-w-2xl">
          <div className="text-center py-4">Task not found</div>
          <div className="flex justify-center">
            <button
              onClick={onClose}
              className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
            >
              Close
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-10">
      <div className="bg-white rounded-lg p-6 w-full max-w-2xl h-4/5 flex flex-col">
        <div className="flex justify-between items-start mb-4">
          {isEditing ? (
            <input
              type="text"
              value={editTitle}
              onChange={(e) => setEditTitle(e.target.value)}
              className="text-2xl font-bold w-full p-1 border border-gray-300 rounded"
            />
          ) : (
            <h2 className="text-2xl font-bold">{task.title}</h2>
          )}
          <button onClick={onClose} className="text-gray-500 hover:text-gray-700">
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18L18 6M6 6l12 12"></path>
            </svg>
          </button>
        </div>

        <div className="mb-4 flex items-center text-sm text-gray-500">
          <span className="mr-3">Status: </span>
          {isEditing ? (
            <select
              value={editStatus}
              onChange={(e) => setEditStatus(e.target.value as TaskStatus)}
              className="p-1 border border-gray-300 rounded"
            >
              <option value={TaskStatus.BACKLOG}>Backlog</option>
              <option value={TaskStatus.IN_PROGRESS}>In Progress</option>
              <option value={TaskStatus.FINISHED}>Finished</option>
            </select>
          ) : (
            <span className="bg-blue-100 text-blue-800 px-2 py-1 rounded-full">
              {task.status.replace('_', ' ')}
            </span>
          )}
        </div>

        <div className="mb-4">
          <div className="text-gray-700 font-medium mb-1">Description</div>
          {isEditing ? (
            <textarea
              value={editDescription}
              onChange={(e) => setEditDescription(e.target.value)}
              className="w-full p-2 border border-gray-300 rounded"
              rows={4}
            />
          ) : (
            <p className="text-gray-600">{task.description || 'No description provided.'}</p>
          )}
        </div>

        <div className="mb-4 text-sm text-gray-500">
          <div>Created: {new Date(task.createdAt).toLocaleString()}</div>
          {task.updatedAt && (
            <div>Last updated: {new Date(task.updatedAt).toLocaleString()}</div>
          )}
        </div>

        <div className="mt-auto overflow-y-auto flex-1">
          {isEditing ? (
            <div className="flex space-x-2 mt-4">
              <button
                onClick={() => setIsEditing(false)}
                className="px-4 py-2 border border-gray-300 text-gray-700 rounded hover:bg-gray-100"
              >
                Cancel
              </button>
              <button
                onClick={handleSaveChanges}
                disabled={isSubmitting}
                className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 disabled:bg-blue-300"
              >
                {isSubmitting ? 'Saving...' : 'Save Changes'}
              </button>
            </div>
          ) : (
            <div className="flex space-x-2 mt-4">
              <button
                onClick={() => setIsEditing(true)}
                className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
              >
                Edit Task
              </button>
              <button
                onClick={handleDeleteTask}
                className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600"
              >
                Delete Task
              </button>
            </div>
          )}

          <CommentList comments={task.comments} />
          <AddCommentForm taskId={task.id} onCommentAdded={handleCommentAdded} />
        </div>
      </div>
    </div>
  );
};

export default TaskDetails;