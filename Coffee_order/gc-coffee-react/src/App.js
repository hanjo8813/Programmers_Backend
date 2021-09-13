import './App.css';
import 'bootstrap/dist/css/bootstrap.css'
import React, {useState} from 'react'
import {ProductList} from "./components/ProductList";
import {Summary} from "./components/Summary";


function App() {
    const [products, setProducts] = useState([
        {id: 'uuid-1', productName: '콜롬비아 커피1', category: '커피빈', price: 3000},
        {id: 'uuid-2', productName: '콜롬비아 커피2', category: '커피빈', price: 3000},
        {id: 'uuid-3', productName: '콜롬비아 커피3', category: '커피빈', price: 3000},
    ]);

    const [items, setItems] = useState([

    ]);

    const handleAddClicked = id => {
        console.log(products.find(v=>v.id === id));
    }

    return (
        <div className="container-fluid">

            <div className="row justify-content-center m-4">
                <h1 className="text-center">Grids &amp; Circle</h1>
            </div>

            <div className="card">
                <div className="row">
                    <div className="col-md-8 mt-4 d-flex flex-column align-items-start p-3 pt-0">
                        <ProductList products={products} onAddClick={handleAddClicked}/>
                    </div>
                    <div className="col-md-4 summary p-4">
                        <Summary items={items}/>
                    </div>
                </div>
            </div>

        </div>
    );
}

export default App;
