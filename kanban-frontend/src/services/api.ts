import axios from 'axios';
import { Task, TaskDTO, CommentDTO, TaskStatus } from '../models/types';

const API_URL = 'http://localhost:8080/api';

// Task API calls
export const fetchAllTasks = async (): Promise<Task[]> => {
  const response = await axios.get(`${API_URL}/tasks`);
  return response.data;
};

export const fetchTasksByStatus = async (status: TaskStatus): Promise<Task[]> => {
  const response = await axios.get(`${API_URL}/tasks/status/${status}`);
  return response.data;
};

export const fetchTaskById = async (id: number): Promise<Task> => {
  const response = await axios.get(`${API_URL}/tasks/${id}`);
  return response.data;
};

export const createTask = async (task: TaskDTO): Promise<Task> => {
  const response = await axios.post(`${API_URL}/tasks`, task);
  return response.data;
};

export const updateTask = async (id: number, task: TaskDTO): Promise<Task> => {
  const response = await axios.put(`${API_URL}/tasks/${id}`, task);
  return response.data;
};

export const updateTaskStatus = async (id: number, status: TaskStatus): Promise<Task> => {
  const response = await axios.patch(`${API_URL}/tasks/${id}/status`, { status });
  return response.data;
};

export const deleteTask = async (id: number): Promise<void> => {
  await axios.delete(`${API_URL}/tasks/${id}`);
};

// Comment API calls
export const fetchCommentsByTaskId = async (taskId: number): Promise<Comment[]> => {
  const response = await axios.get(`${API_URL}/comments/task/${taskId}`);
  return response.data;
};

export const createComment = async (comment: CommentDTO): Promise<Comment> => {
  const response = await axios.post(`${API_URL}/comments`, comment);
  return response.data;
};

export const updateComment = async (id: number, comment: CommentDTO): Promise<Comment> => {
  const response = await axios.put(`${API_URL}/comments/${id}`, comment);
  return response.data;
};

export const deleteComment = async (id: number): Promise<void> => {
  await axios.delete(`${API_URL}/comments/${id}`);
};