// import { ReactNode } from "react";
import { useEffect, useState } from "react";

// import { MarketDepthRow } from "./useMarketDepthData";


interface QuantityCellProps {
    quantity: number; // Current quantity
    previousQuantity: number | null; // Previous quantity to compare with
    type: 'bid' | 'ask'; // Differentiate between bid and ask quantity
}

export const QuantityCell = ({quantity, previousQuantity, type}: QuantityCellProps) => {
    const [width, setWidth] = useState<number>(0); // Width of the colour bar
    const [changeDirection, setDirection] = useState<'increase' | 'decrease' | null>(null);

    useEffect(() => {
        // Set initial width
        setWidth((quantity / 5000) * 100);

        // Check if quantity has increased or decreases
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

    // Class based on type
    const baseClass = type === 'bid' ? 'bid-quantity' : 'ask-quantity';

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