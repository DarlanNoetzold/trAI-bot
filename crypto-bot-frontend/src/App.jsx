// App principal 
import React from "react";
import { BrowserRouter as Router } from "react-router-dom";
import AppRouter from "./router/AppRouter";
import "./App.css";

const App = () => {
  return (
    <Router>
      <AppRouter />
    </Router>
  );
};

export default App;