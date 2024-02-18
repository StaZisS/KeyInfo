import {Day} from "./application/day";
import {useQuery} from "react-query";
import ApplicationService from "../../../services/ApplicationService";
import {Loading} from "../../snippets/Loading";
import {useState} from "react";
import {Container} from "react-bootstrap";
import {Time} from "./application/time";
import {ApplicationItem} from "./application/applicationItem";

export const ApplicationList = () => {

    const [start, setStart] = useState()
    const [end, setEnd] = useState()
    const [build, setBuild] = useState("")
    const [room, setRoom] = useState("")

    const normalizeDate = (data) => {
        const date = new Date(data)

        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0'); // добавляем 1, так как месяцы в JS нумеруются с 0
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        const seconds = String(date.getSeconds()).padStart(2, '0');

        return {year, month, day, hours, minutes, seconds}
    }

    const {
        data,
        isLoading,
        error
    } = useQuery(['applications'], (params) => ApplicationService.getApplications({
        start: params.start,
        end: params.end,
        buildId: params.buildId,
        roomId: params.roomId
    }), {
        select: ({data}) => data,
        refetchOnWindowFocus: false,
        keepPreviousData: true
    })

    if (isLoading) {
        return (
            <Loading/>
        )
    }

    if (error) {
        <>Ошибка запроса заявок</>
    }


    if (!data.length){
        return(
            <Container className='text-center text-danger fs-1 fw-bold mt-3'>Нет заявок</Container>
        )
    }

    return (
        <div className='mt-5'>
            {data.map((item) => (
                <ApplicationItem name={item.owner_name}
                                 email={item.owner_email}
                                 build={item.build_id}
                                 room={item.room_id}
                                 start={normalizeDate(item.start_time)}
                                 end={normalizeDate(item.end_time)}
                                 dublicate={item.is_duplicate}
                                 key={item.application_id}
                                 id={item.application_id}
                />

            ))}
        </div>
    )
}