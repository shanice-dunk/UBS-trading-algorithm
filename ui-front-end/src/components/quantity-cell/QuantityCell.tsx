// import { ReactNode } from "react";
import { useEffect, useState } from "react";
import "../market-depth/MarketDepthCSS.css";

// import { MarketDepthRow } from "./useMarketDepthData";

// props
interface QuantityCellProps {
    quantity: number; // Current quantity
    previousQuantity: number | null; // Previous quantity to compare with
    type: 'bid' | 'ask'; // Differentiate between bid and ask quantity
}

// Component, takes quantity, previousQuantity & type as props
export const QuantityCell = ({quantity, previousQuantity, type}: QuantityCellProps) => {
    // State variable that controls width of bar
    const [width, setWidth] = useState<number>(0); 
    // State varialbe to indicate of quantity has increased/decreased/unchanged
    const [changeDirection, setDirection] = useState<'increase' | 'decrease' | null>(null);

    // Hook 
    useEffect(() => {
        // Set initial width
        setWidth((quantity / 5000) * 100);

        // Check if quantity has increased or decreased or unchanged
        if (previousQuantity !== null) {
            if (quantity > previousQuantity) {
                setDirection('increase');
            } else if (quantity < previousQuantity) {
                setDirection('decrease');
            } else {
                setDirection(null);
            }
        }
    }, [quantity, previousQuantity]);

    // Styling depending on type (bid or ask)
    const baseClass = type === 'bid' ? 'bid-quantity' : 'ask-quantity';

    // Rendering table
    return (
        <td className={`${baseClass}`}>
            <div
            className={`quantity-bar ${changeDirection}`}
            style={{width: `${width}%`}} // Adjust width of the bar
            >
                {quantity}
            </div>
        </td>
    );
  
};