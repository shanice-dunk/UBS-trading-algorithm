import { ReactNode } from "react";
import { MarketDepthRow } from "./useMarketDepthData";

interface PriceCellProps {
    price: ReactNode;
    data: MarketDepthRow[];
}

export const PriceCell = (props: PriceCellProps) => {
    return <td>{props.price}</td>;

};