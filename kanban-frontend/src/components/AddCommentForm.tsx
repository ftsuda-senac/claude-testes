import React, { useState } from 'react';
import { createComment } from '../services/api';

interface AddCommentFormProps {
  taskId: number;
  onCommentAdded: () => void;
}

const AddCommentForm: React.FC<AddCommentFormProps> = ({ taskId, onCommentAdded }) => {
  const [content, setContent] = useState('');
  const [author, setAuthor] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!content.trim() || !author.trim()) return;

    try {
      setIsSubmitting(true);
      await createComment({
        content,
        author,
        taskId
      });
      setContent('');
      onCommentAdded();
    } catch (error) {
      console.error('Error adding comment:', error);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="mt-4">
      <h3 className="text-lg font-medium mb-3">Add Comment</h3>
      <div className="mb-3">
        <input
          type="text"
          placeholder="Your name"
          value={author}
          onChange={(e) => setAuthor(e.target.value)}
          className="w-full p-2 border border-gray-300 rounded"
          required
        />
      </div>
      <div className="mb-3">
        <textarea
          placeholder="Write a comment..."
          value={content}
          onChange={(e) => setContent(e.target.value)}
          className="w-full p-2 border border-gray-300 rounded"
          rows={3}
          required
        />
      </div>
      <button
        type="submit"
        disabled={isSubmitting}
        className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 disabled:bg-blue-300"
      >
        {isSubmitting ? 'Adding...' : 'Add Comment'}
      </button>
    </form>
  );
};

export default AddCommentForm;