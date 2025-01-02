
import { BrowserRouter, Route, Routes } from 'react-router-dom'
import './App.css'
import LoginPage from './pages/login/LoginPage'
import ProtectedPages from './pages/extra/ProtectedPages'
import RestaurantsPage from './pages/restaurant/RestaurantsPage'
import ProfilePage from './pages/profile/ProfilePage'
import RestaurantPage from './pages/restaurant/RestaurantPage'
import PageNotFound from './pages/extra/PageNotFound'

function App() {

  return (
    <BrowserRouter>
    
    <Routes>
      <Route path="/" element={<LoginPage />} />
      <Route path="protected" element={<ProtectedPages />}>
        <Route path='restaurants' index element={<RestaurantsPage />} />
        <Route path='restaurants/:id' index element={<RestaurantPage />} />
        <Route path='profile/:id' index element={<ProfilePage />} />
      </Route>
      <Route path="*" element={<PageNotFound />} />
    </Routes>
  </BrowserRouter>
  )
}

export default App
