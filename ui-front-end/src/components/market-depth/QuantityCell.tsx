// import { ReactNode } from "react";

// import { MarketDepthRow } from "./useMarketDepthData";


interface QuantityCellProps {
    quantity: number; // Current quantity
    previousQuantity: number | null; // Previous quantity to compare with
}

export const QuantityCell = ({quantity, previousQuantity}: QuantityCellProps) => {
    // Determine arrow direction based on quantity change
    const getQuantityArrow = () => {
        if (previousQuantity === null) return null; // No arrow for first render
        if (quantity > previousQuantity) return <span className="arrowUp">⬆</span>; // Quantity increased
        if (quantity < previousQuantity) return <span className="arrowDown">⬇</span>; // Quantity decreased
        return "➖"; // No change
    };

    return (
        <td>
            {quantity} <span className="arrow">{getQuantityArrow()}</span>
        </td>
    )
}