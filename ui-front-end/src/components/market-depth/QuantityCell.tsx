import { ReactNode } from "react";
import { MarketDepthRow } from "./useMarketDepthData";


interface QuantityCellProps {
    quantity: ReactNode;
    data: MarketDepthRow[];
}

export const QuantityCell = (props: QuantityCellProps) => {
    return <td>{props.quantity}</td>
}