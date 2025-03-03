import React from 'react';
import { Comment } from '../models/types';

interface CommentItemProps {
  comment: Comment;
}

const CommentItem: React.FC<CommentItemProps> = ({ comment }) => {
  // Format date for display
  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleString();
  };

  return (
    <div className="border-b border-gray-200 pb-3 mb-3">
      <div className="flex justify-between items-start">
        <p className="font-medium">{comment.author}</p>
        <span className="text-xs text-gray-500">{formatDate(comment.createdAt)}</span>
      </div>
      <p className="mt-1 text-gray-700">{comment.content}</p>
    </div>
  );
};

export default CommentItem;