
import React from 'react'
import { useParams } from 'react-router-dom'

const RestaurantPage = () => {
  const { id } = useParams()
  return (
    <div>RestaurantPage {id}</div>
  )
}

export default RestaurantPage