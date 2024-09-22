// import { ReactNode, useEffect, useState } from "react";
// import { MarketDepthRow } from "./useMarketDepthData";
// import { MarketDepthPanel } from "./MarketDepthPanel";
import './MarketDepthFeature.css'
interface PriceCellProps {
    price: number; // The current price
    previousPrice: number | null; // Previous price to compare with
}

export const PriceCell = ({price, previousPrice} : PriceCellProps) => {

    const getPriceArrow = () => {
        if (previousPrice === null) return null; // No arrow for the first render
        if (price > previousPrice) return <span className="arrowUp">⬆</span>; // Price increased
        if (price < previousPrice) return <span className="arrowDown">⬇</span>; // Price decreased
        return "➖"; // No change
    };

    return (
        <td>
            {price} <span className="arrow">{getPriceArrow()}</span>
        </td>
    );
};
