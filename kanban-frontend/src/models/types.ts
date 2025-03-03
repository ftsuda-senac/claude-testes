export enum TaskStatus {
    BACKLOG = "BACKLOG",
    IN_PROGRESS = "IN_PROGRESS",
    FINISHED = "FINISHED"
  }
  
  export interface Comment {
    id: number;
    content: string;
    author: string;
    createdAt: string;
  }
  
  export interface Task {
    id: number;
    title: string;
    description: string;
    status: TaskStatus;
    createdAt: string;
    updatedAt?: string;
    comments: Comment[];
  }
  
  export interface ColumnType {
    title: string;
    status: TaskStatus;
  }
  
  export interface CommentDTO {
    content: string;
    author: string;
    taskId: number;
  }
  
  export interface TaskDTO {
    title: string;
    description: string;
    status?: TaskStatus;
  }