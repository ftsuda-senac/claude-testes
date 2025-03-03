import React from 'react';
import { Comment } from '../models/types';
import CommentItem from './CommentItem';

interface CommentListProps {
  comments: Comment[];
}

const CommentList: React.FC<CommentListProps> = ({ comments }) => {
  if (comments.length === 0) {
    return <p className="text-gray-500 italic">No comments yet.</p>;
  }

  return (
    <div className="mt-4">
      <h3 className="text-lg font-medium mb-3">Comments</h3>
      <div className="max-h-64 overflow-y-auto">
        {comments.map(comment => (
          <CommentItem key={comment.id} comment={comment} />
        ))}
      </div>
    </div>
  );
};

export default CommentList;