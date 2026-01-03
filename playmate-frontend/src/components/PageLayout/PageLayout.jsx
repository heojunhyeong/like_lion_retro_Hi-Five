import Header from "../Header/Header";
import "./PageLayout.css";
import { Outlet } from "react-router-dom";

const PageLayout = () => {
  return (
    <div className="page-wrapper">
      <Header />
      <main className="page-content">
        <Outlet />
      </main>
    </div>
  );
};

export default PageLayout;
