import React from 'react';
import {render} from 'react-dom';
import KanbanBoard from './KanbanBoard';

let cardsList = [
  {
    id: 1,
    title: "Sprawdzić wszystko ponownie",
    description: "Upewnić się czy wszystko działa poprawnie",
    status: "in-progress",
    tasks: []
  },
  {
    id: 2,
    title: "Sprawdzić todo",
    description: "Upewnić się czy wszystko działa poprawnie",
    status: "todo",
    tasks: [
      {
        id: 1,
        name: "Przykłady",
        done: true
      }/*,
      {
        id: 2,
        name: "Przykład Kanban",
        done: false
      },
      {
        id: 3,
        name: "Moje eksperymenty",
        done: false
      }
	  */
    ]
  }
];

render(<KanbanBoard cards={cardsList} />, document.getElementById('root'));
