import React, { Component } from 'react';
import List from './List';

class KanbanBoard extends Component {
  render(){
    return (
      <div className="app">
        <List id='todo'
              title="Do zrobienia"
              cards={this.props.cards.filter((card) => card.status === "todo")} />
        <List id='in-progress'
              title="W toku"
              cards={this.props.cards.filter((card) => card.status === "in-progress")} />
        <List id='done'
              title='Zrobione'
              cards={this.props.cards.filter((card) => card.status === "done")} />
		 <List id='hardissue'
              title='Trudne sprawy :)'
              cards={this.props.cards.filter((card) => card.status === "hardissue")} />
      </div>
    );
  }
};
export default KanbanBoard;
