// import { ReactNode } from "react";

// import { MarketDepthRow } from "./useMarketDepthData";


interface QuantityCellProps {
    quantity: number; // Current quantity
    previousQuantity: number | null; // Previous quantity to compare with
}

export const QuantityCell = ({quantity, previousQuantity}: QuantityCellProps) => {
    const getQuantityChange = () => {
        if (previousQuantity === null) return ""; // No class for first render
        if (quantity > previousQuantity) return "quantityIncrease"; // Quantity increased
        if (quantity < previousQuantity) return "quantityDecrease"; // Quantity decreased
        return ""; // No change
    };

    return (
        <td className={getQuantityChange()}>
            {quantity}
        </td>
    );
};